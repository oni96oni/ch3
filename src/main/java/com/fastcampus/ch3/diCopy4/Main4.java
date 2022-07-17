package com.fastcampus.ch3.diCopy4;

import com.google.common.reflect.ClassPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component class Car {
    @Resource
    Engine engine;
//    @Resource
    Door door;

    @Override
    public String toString() {
        return "Car{" +
                "engine=" + engine +
                ", door=" + door +
                '}';
    }
}
@Component class SportsCar extends Car{}
@Component class Truck extends Car{}
@Component class Engine {}
@Component class Door {}

class Appcontext {
    Map map; //객체 저장소

    Appcontext() {
        map = new HashMap();
        doComponenetScan();
        doAutowired();
        doResource();
    }

    private void doResource() {
        // ★map에 저장된 객체의 iv 중에 @Resource가 붙어 있으면 map에서 iv의 이름에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 저장하는것)
        try {
            for(Object bean : map.values()) {
                for(Field fid : bean.getClass().getDeclaredFields()) {
                    if(fid.getAnnotation(Resource.class)!=null) { // byName
                        fid.set(bean, getBean(fid.getName())); // car.engine = obj;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    //코파일럿은 어떤 객체를 어떤 객체에 연결하는지 알려주는 코드를 작성한다.
    //@Autowired 어노테이션이 붙은 필드는 자동으로 연결된다.
    private void doAutowired() {
        // ★map에 저장된 객체의 iv 중에 @Autowired가 붙어 있으면 map에서 iv의 타입에 맞는 객체를 찾아서 연결(객체의 주소를 iv에 저장하는것)
        try {
            for(Object bean : map.values()) {
                for(Field fid : bean.getClass().getDeclaredFields()) { // getDeclaredFields() : 자식 클래스에서도 상속받은 필드를 가져옴
                    if(fid.getAnnotation(Autowired.class)!=null) { // byType @Autowired가 붙어있는 태그가 있으면 다음코드 실행
                        fid.set(bean, getBean(fid.getType())); // car.engine = obj; 필드의 값을 바꾸어준다.
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void doComponenetScan() {
        // 1. 패키지내의 클래스 목록을 가져온다.
        // 2. 반복문으로 클래스를 하나씩 읽어와서 @Component이 붙어있는지 확인
        // 3. @Component가 붙어있으면 객체를 생성해서 map에 저장
        try {
            ClassLoader classLoader = Appcontext.class.getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);

            Set<ClassPath.ClassInfo> set = classPath.getTopLevelClasses("com.fastcampus.ch3.diCopy4");

            for(ClassPath.ClassInfo classinfo :set){
                Class clazz = classinfo.load();
                Component component = (Component) clazz.getAnnotation(Component.class);
                if (component != null) {
                    String id = StringUtils.uncapitalize(classinfo.getSimpleName());
                    map.put(id, clazz.newInstance());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Object getBean(String key) { // 객체를 검색할때 byName으로 검색하는것
        return map.get(key);
    }

    Object getBean(Class clazz) { // 객체를 검색할때 byType으로 검색하는것
        for(Object obj : map.values()) {
            if(clazz.isInstance(obj)) {
                return obj;
            }
        }
        return null;
    }
}

public class Main4 {
    public static void main(String[] args) throws Exception {
        Appcontext ac = new Appcontext();
        Car car = (Car)ac.getBean("car"); // 객체를 검색할때 byName으로 검색하는것
        Engine engine = (Engine)ac.getBean("engine");
        Door door = (Door) ac.getBean(Door.class); // 객체를 검색할때 byType으로 검색하는것

        // 수동으로 객체를 연결 애너테이션을 사용(스프링)함으로써 관리해야할 코드를 줄일 수 있다. 아래가 수동대입하는 코드
//        car.engine = engine;
//        car.door = door;

        System.out.println("car = " + car);
        System.out.println("engine = " + engine);
        System.out.println("door = " + door);
    }
}
