package net.cserny.videosmover.learning;

import org.junit.Test;

public class LearnCallbacksTest {
    @Test
    public void passCallbackToMethod() throws Exception {
        System.out.println(process("hello", new FirstCallback()));
        System.out.println(process("hello 2nd", new SecondCallback()));
    }

    @Test
    public void passCallbackLambda() throws Exception {
        System.out.println(process("hello", query -> query + " Some text"));
        System.out.println(process("hello", query -> query + " Some other text"));
    }

    private String process(String query, MyCallback callback) {
        return callback.doSomething(query);
    }

    private interface MyCallback {
        String doSomething(String query);
    }

    private static class FirstCallback implements MyCallback {
        @Override
        public String doSomething(String query) {
            return "First " + query;
        }
    }

    private static class SecondCallback implements MyCallback {
        @Override
        public String doSomething(String query) {
            return "Second " + query;
        }
    }
}
