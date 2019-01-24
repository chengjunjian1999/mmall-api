package se;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    class A{}

    class B extends A{}

    class C extends A{}

    class D extends B{}

    public static void main(String[] args) {

        A obj = new Test().new B();
        System.out.println(obj instanceof B);

        System.out.println(obj instanceof C);

        System.out.println(obj instanceof D);

        System.out.println(obj instanceof A);

    }
}
