package com.wisekrakr.firstgame;

/**
 * Created by David on 11/23/2017.
 */

/*
public @interface pastShit {

    @Override
    public void create () {

        int x = 0;
        int y = 0;
        final Player player = new Player(" David " ,new Vector2(100,10));
        final Enemy enemy = new Enemy(" Bitch ", new Vector2(x,y));
        final Enemy enemy2 = new Enemy(" Bastard ", new Vector2(x,y));

        final Array<BaseObject> baseObjectArray = new Array<BaseObject>();
        baseObjectArray.addAll();
        final Array<Asteroid>asteroidsArray = new Array<Asteroid>(100);
        int asteroidCount = 20;
        for(int i = 0; i < asteroidCount; i++){
            Asteroid asteroid = new Asteroid("Asteroid " + i, new Vector2(x, y));
            System.out.println(asteroid.getName() + asteroid.getPosition());
            //asteroid.movement();
        }

        Runnable stepAction = new Runnable() {
            @Override
            public void run() {
                player.movement();
                System.out.println(player.getName() + player.getPosition());
                enemy.movement();
                System.out.println(enemy.getName() + enemy.getPosition());
                enemy2.movement();
                System.out.println(enemy2.getName() + enemy2.getPosition());
                player.collisionDetector(enemy);
                player.collisionDetector(enemy2);
                asteroidsArray.addAll();

            }
        };
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(stepAction,0,1, TimeUnit.SECONDS);


    }

    @Override
    public void dispose () {

    }
}
*/