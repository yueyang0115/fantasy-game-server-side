package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.*;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MonsterMangerTest {
    private MonsterManger myMonsterManger;
    private Session session;

    public MonsterMangerTest(){
        this.session = createSession();
        this.myMonsterManger = new MonsterManger(this.session);
    }

    private Session createSession() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session;
    }

    @Test
    public void testAll(){
        initMonster();
        getMonsterTest();
        getMonstersTest();
        setMonsterHpTest();

        //session.getTransaction().commit();
        //session.close(); call session.close() will write data to database
        //HibernateUtil.shutdown();
    }

    public void initMonster(){
        session.beginTransaction();
        TerrainDAO terrainHandler = new TerrainDAO(session);
        terrainHandler.initialTerrain();

        Territory t = new Territory(1111, 222, 3333, "explored");
        Monster m = new Monster("wolf", 97, 10);
        Monster m2 = new Monster("wolf", 98, 10);
        t.addMonster(m);
        t.addMonster(m2);

        t.setTerrain(terrainHandler.getRandomTerrain());
        session.save(t);

        Long count = (Long) session.createQuery("select count(*) from Monster ").uniqueResult();
        System.out.println("Monster num in database is "+count.intValue());
    }

    public void getMonsterTest(){
        Monster m = myMonsterManger.getMonster(1);
        assertNotNull(m);
    }

    public void getMonstersTest(){
        List<Monster> monsterList = myMonsterManger.getMonsters(1);
        System.out.println("Monster num in territoryID = 1 is "+monsterList.size());
        Monster m = monsterList.get(0);
        assertNotNull(m);
    }

    public void setMonsterHpTest(){
        myMonsterManger.setMonsterHp(1,90);
        Monster m = myMonsterManger.getMonster(1);
        assertEquals(m.getHp(),90);
    }

}
