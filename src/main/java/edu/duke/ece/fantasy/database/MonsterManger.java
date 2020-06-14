package edu.duke.ece.fantasy.database;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class MonsterManger {
    private Session session;

    public MonsterManger(Session session) {
        this.session = session;
    }

    public void addMonster(Monster m, WorldCoord where){
        m.setCoord(where);
        session.save(m);
    }

    //get a monster from database based on the provided monsterID
    public Monster getMonster(int monsterID) {
        Query q = session.createQuery("From Monster M where M.id =:id");
        q.setParameter("id", monsterID);
        Monster res = (Monster) q.uniqueResult();
        return res;
    }

    //get all monsters in the provided coord from database
    public List<Monster> getMonsters(int territoryID){
        List<Monster> monsterList = new ArrayList<>();
        Query q = session.createQuery("From Territory T where T.id =:territoryID");
        q.setParameter("territoryID", territoryID);
        Territory t = (Territory) q.uniqueResult();

        WorldCoord coord = t.getCoord();
        Query q2 = session.createQuery("From Monster M where M.coord =:coord");
        q2.setParameter("coord", coord);
        for(Object o : q2.list()) {
            monsterList.add((Monster) o);
        }
        return monsterList;
    }

    //update a monster's hp
    public boolean setMonsterHp(int monsterID, int hp){
        Monster m = getMonster(monsterID);
        if (m == null) { // don't have that monster
            return false;
        }
        m.setHp(hp);
        session.update(m);
        return true;
    }

    //delete a monster from database
    public void deleteMonster(int monsterID){
        Monster monster;
        if ((monster = (Monster) session.get(Monster.class, monsterID)) != null) {
            session.delete(monster);
            System.out.println("[DEBUG] Delete monster " + monsterID);
        }
    }
}
