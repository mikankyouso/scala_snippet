public class Generic {
    public static class Func<R, P> {
        public R apply(P p) {
            return null;
        }
    }

    void foo() {
        Func<Number, Number> f = new Func<Number, Number>();
        Func<? extends Number, ? super Number> f2 = f;
        invoke(f);
        invoke(f2);
    }
    
    Number invoke(Func<? extends Number, ? super Number> f) {
        return f.apply(3);
    }
    
}
