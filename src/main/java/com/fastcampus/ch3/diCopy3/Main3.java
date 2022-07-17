package com.fastcampus.ch3.diCopy3;

import com.google.common.reflect.ClassPath;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component class Car {}
@Component class SportsCar extends Car{}
@Component class Truck extends Car{}
@Component class Engine {}

class Appcontext {
    Map map; //객체 저장소

    Appcontext() {
        map = new HashMap();
        doComponenetScan();
    }

    private void doComponenetScan() {
        // 1. 패키지내의 클래스 목록을 가져온다.
        // 2. 반복문으로 클래스를 하나씩 읽어와서 @Component이 붙어있는지 확인
        // 3. @Component가 붙어있으면 객체를 생성해서 map에 저장
        try {
            ClassLoader classLoader = Appcontext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.fastcampus.ch3.diCopy3");//클래스 목록 가져온다.

            for(ClassPath.ClassInfo classInfo : set){ //for문으로 set에 담긴 클래스들을 하나씩 읽는다. @Component가 붙어있는지 확인한다.
                Class clazz = classInfo.load();
                Component component = (Component) clazz.getAnnotation(Component.class);
                if (component != null) {
                    String id = StringUtils.uncapitalize(classInfo.getSimpleName()); //getSimpleName - 클래스 이름을 얻어온다. uncapitalize - 소문자로 변환한다.
                    map.put(id, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object getBean(String key) { // 객체를 검색할때 byName으로 검색하는것
        return map.get(key);
    } // 객체를 검색할때 byName으로 검색하는것

    Object getBean(Class clazz) { // 객체를 검색할때 byType으로 검색하는것
        for(Object obj : map.values()) { // 반복문을 이용해서 map에 있는 values(타입)들을 갖고온다.
            if(clazz.isInstance(obj)) {
                return obj;
            }
        }
        return null;
    }
}

public class Main3 {
    public static void main(String[] args) throws Exception {
        Appcontext ac = new Appcontext();
        Car car = (Car)ac.getBean("car"); // 객체를 검색할때 byName으로 검색하는것
        Car car2 = (Car)ac.getBean(Car.class); // 객체를 검색할때 byType으로 검색하는것
        Engine engine = (Engine)ac.getBean("engine");
        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
    }
}
