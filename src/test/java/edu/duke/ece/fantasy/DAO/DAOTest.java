package edu.duke.ece.fantasy.DAO;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.MonsterDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.SoldierDAO;
import edu.duke.ece.fantasy.database.DAO.UnitDAO;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DAOTest {
    public static Session session;
    private PlayerDAO playerDAO;
    private SoldierDAO soldierDAO;
    private MonsterDAO monsterDAO;
    private UnitDAO unitDAO;

    @BeforeAll
    public static void setUpSession(){
        //System.out.println("executing beforeAll in DAOTest");
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
    }

    @AfterAll
    public static void closeSession(){
        //System.out.println("executing afterAll in DAOTest");
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    public void TestAll(){
        playerDAO = new PlayerDAO(session);
        monsterDAO = new MonsterDAO(session);
        soldierDAO = new SoldierDAO(session);
        unitDAO = new UnitDAO(session);
        playerDAO.addPlayer("testname","testpassword");

        testPlayerDAO();
        testMonsterDAO();
        testSoldierUnitDAO();
    }

    public void testPlayerDAO(){
        //System.out.println("test playerDAO");

        Player p = playerDAO.getPlayer("testname");
        int id = p.getId();
        int wid = p.getWid();
        assertEquals(playerDAO.getPlayer(id).getUsername(),playerDAO.getPlayerByWid(wid).getUsername());
    }

    public void testMonsterDAO(){
        //System.out.println("test monsterDAO");

        Monster m1 = new Monster("wolf",60,6,10);
        Monster m2 = new Monster("wolf",70,7,20);
        WorldCoord coord1 = new WorldCoord(1,1,1);
        WorldCoord coord2 = new WorldCoord(1,1,2);
        monsterDAO.addMonster(m1,coord1);
        monsterDAO.addMonster(m2,coord2);

        Monster DBmonster1 = monsterDAO.getMonsters(coord1).get(0);
        int id = DBmonster1.getId();
        int id2 = monsterDAO.getMonsters(coord2).get(0).getId();
        assertEquals(monsterDAO.getMonster(id), DBmonster1);
        assertEquals(monsterDAO.countMonstersInRange(coord1,4,4),2);
        List<Monster> DBmonsterList = monsterDAO.getMonstersInRange(new WorldCoord(1,0,0),4,4);
        assertEquals(DBmonsterList.size(),2);

        monsterDAO.setMonsterHp(id,100);
        monsterDAO.setMonsterStatus(id,false);
        monsterDAO.setMonstersStatus(DBmonsterList,false);
        monsterDAO.updateMonsterCoord(id,-1,1);
        assertEquals(monsterDAO.getMonster(id).getHp(),100);
        assertEquals(monsterDAO.getMonster(id).getCoord(),new WorldCoord(1,-1,1));
        assertEquals(monsterDAO.getMonster(id).isNeedUpdate(),false);
        assertEquals(monsterDAO.getMonster(id2).isNeedUpdate(),false);
    }

    public void testSoldierUnitDAO(){
        //System.out.println("test soldierDAO");

        Player p = playerDAO.getPlayer("testname");
        int playerID = p.getId();
        List<Soldier> soldierDAOList = soldierDAO.getSoldiers(playerID);
        int soldierID = soldierDAOList.get(0).getId();
        Soldier soldier = soldierDAO.getSoldier(soldierID);
        assertEquals(soldier,soldierDAOList.get(0));
        assertEquals(soldier.getHp(),soldierDAOList.get(0).getHp());

        Unit unit = unitDAO.getUnit(soldierID);
        assertEquals(unit.getHp(), soldier.getHp());
        assertEquals(unit.getSpeed(), soldier.getSpeed());
        int hp = unit.getHp();
        unitDAO.setUnitHp(soldierID,hp-5);
        assertEquals(unitDAO.getUnit(soldierID).getHp(),hp-5);
        unitDAO.deleteUnit(soldierID);
        assertEquals(session.get(Unit.class, soldierID),null);
    }

}
