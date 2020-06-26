package edu.duke.ece.fantasy.json;

import edu.duke.ece.fantasy.database.Unit;
import java.util.List;

public class BattleAction {
    private Unit attacker;
    private Unit attackee;
    private String actionType = "normal"; //"normal", "magical"
    private List<Integer> units;

    public BattleAction() {
    }

    public BattleAction(Unit attacker, Unit attackee, String actionType, List<Integer> units) {
        this.attacker = attacker;
        this.attackee = attackee;
        this.actionType = actionType;
        this.units = units;
    }

    public Unit getAttacker() { return attacker; }

    public void setAttacker(Unit attacker) { this.attacker = attacker; }

    public Unit getAttackee() { return attackee; }

    public void setAttackee(Unit attackee) { this.attackee = attackee; }

    public String getActionType() { return actionType; }

    public void setActionType(String actionType) { this.actionType = actionType; }

    public List<Integer> getUnits() { return units; }

    public void setUnits(List<Integer> units) { this.units = units; }
}