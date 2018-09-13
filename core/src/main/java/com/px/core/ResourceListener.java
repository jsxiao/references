package com.px.core;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResourceListener {
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
    private WatchService ws;

    private ResourceListener() {
        try {
            ws = FileSystems.getDefault().newWatchService();
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        fixedThreadPool.execute(new Listener(ws));
    }

    public static void addListener(String... path) throws IOException {
        ResourceListener resourceListener = new ResourceListener();
        for(String p : path){
            Path _path = Paths.get(p);
            _path.register(resourceListener.ws, StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_CREATE);
        }
    }

    public static void main(String[] args) {
        try {
            String[] path = new String[]{"/Users/Jason/Documents/repository/git/oschina/dsh/pux-service/src/main/resources", "/Users/Jason/Documents/repository/git/oschina/dsh/pux-front-all/src/main/resources"};
            ResourceListener.addListener(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Listener implements Runnable {
    private WatchService service;

    public Listener(WatchService service) {
        this.service = service;
    }

    public void run() {
        try {
            while (true) {
                WatchKey watchKey = service.take();
                Path path = (Path) watchKey.watchable();

                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> event : watchEvents) {

                    // TODO 根据事件类型采取不同的操作
                    //System.out.println("[" + rootPath + "/" + event.context() + "]文件发生了[" + event.kind() + "]事件");


                    if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY){
                        String finalFilePath = path.toString() + "/" + event.context();
                        if(finalFilePath.endsWith(".properties")){
                            PropertiesLoader.INSTANCE.reLoad(finalFilePath);
                        }
                    }
                }
                watchKey.reset();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                service.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}