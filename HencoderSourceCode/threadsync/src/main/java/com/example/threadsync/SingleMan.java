package com.example.threadsync;

public class SingleMan {
    //这里的 volatile 是实现在初始调用 SingleMan 构造方法进行初始化时，只有在构造方法调用完成之后才会将 sInstance 标记为可用状态（即不为 null）
    //也就是说，如果没有 volatile 关键字，在 SingleMan 还没调用完成时，sInstance 就会被标记为可用状态
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


    //还有一个小问题对象的初始化是一个很复杂的过程，可能会出现，类已经创建了，但是里面的内容还没初始化，即对象正在初始化过程中，但是没有初始化完，但此时该实例在虚拟机中已经被标记为可用了。此时如果有另一个线程来访问 newInstance3 方法，在第一处判 null 会返回 false，该线程就会拿到一个还没有初始化完成的对象去使用，
    //需要使用 volatile 来修饰 sInstance 成员，这样只有在 SingleMan 初始化完成之后才会将 sInstance 标记为可用。
    static SingleMan newInstance3() {
        //下面的判断是为了让 sInstance 执行初始化之后就不要在进行初始化了。
        if (sInstance == null) {
            synchronized(SingleMan.class) {
                //这里的判断是为了当有多个线程排队等待创建 SingleMan 时，只有排队的第一个会创建 SingleMan 对象
                if(sInstance == null) {
                    sInstance = new SingleMan();
                }
            }
        }
        return sInstance;
    }


}
