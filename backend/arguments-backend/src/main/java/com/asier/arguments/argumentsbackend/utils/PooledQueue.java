package com.asier.arguments.argumentsbackend.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This component allows to create a queue of resources and process it with multithreading
 * @param <T> Resource class
 */
@Component
public abstract class PooledQueue<T> implements Runnable{
    private final ArrayBlockingQueue<T> entityQueue = new ArrayBlockingQueue<>(1000);
    private final ExecutorService pool;
    private final int threads;

    public PooledQueue(int threads){
        this.threads = threads;
        pool = Executors.newFixedThreadPool(threads);
    }
    public synchronized void enqueue(T entity){
        entityQueue.add(entity);
    }

    @PostConstruct
    public void init(){
        for(int i = 0; i < threads; i++)
            pool.submit(this);
    }

    protected T takeFromQueue() throws InterruptedException {
        return entityQueue.take();
    }
}
