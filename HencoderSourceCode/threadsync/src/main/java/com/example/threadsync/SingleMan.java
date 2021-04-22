package com.example.threadsync;

public class SingleMan {
    //
    private static volatile SingleMan sInstance;

    private SingleMan() {
    }

    static SingleMan newInstance() {
        if (sInstance == null) {
            synchronized (SingleMan.class) {
                if (sInstance == null) {
                    sInstance = new SingleMan();
                }
            }
        }
        return sInstance;
    }

    //凡是使用 synchronzied 做线程同步，性能都是比较低的，这就导致每次调用 newInstance1 获取 sInstance 对象都会比较慢，即使 sInstance 已经初始化完毕了，也依然会很慢，因此不能将整个方法都用 synchronzied 锁住，只需在 sInstance 为 null，需要初始化的时候使用 synchronzied 锁住即可，如 newInstance2 所示
    static synchronized SingleMan newInstance1() {
        if (sInstance == null) {
            sInstance = new SingleMan();
        }
        return sInstance;
    }

    //先检查 sInstance 是否为 null，如果为 null 在执行初始化操作。
    //不过这种写法会有另外的问题，线程 1 访问此方法，发现 sInstance 为 null，然后获取锁，开始执行初始化，此时线程 2 也访问此方法，sInstance 也为 null，但由于获取不到锁，只能等着，等到线程 1 执行 sInstance 初始化完毕了，释放锁，此时线程 2 接着获取锁，执行初始化操作。即 sInstance 会被多次初始化，显然是浪费性能的。
    //因此需要在获取锁之后再次判断 sInstance 是否为 null，如果不为 null 才执行初始化，如 newInstance3
    static SingleMan newInstance2() {
        if (sInstance == null) {
            synchronized(SingleMan.class) {
                sInstance = new SingleMan();
            }
        }
        return sInstance;
    }


    static SingleMan newInstance3() {
        //下面的判断是为了让 sInstance 执行初始化之后就不要在进行初始化了。
        if (sInstance == null) {
            synchronized(SingleMan.class) {
                if(sInstance == null) {
                    sInstance = new SingleMan();
                }
            }
        }
        return sInstance;
    }


}
