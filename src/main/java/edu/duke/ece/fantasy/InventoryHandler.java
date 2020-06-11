package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.json.AttributeRequestMessage;
import edu.duke.ece.fantasy.json.AttributeResultMessage;
import edu.duke.ece.fantasy.json.InventoryRequestMessage;
import edu.duke.ece.fantasy.json.InventoryResultMessage;
import org.hibernate.Session;

public class InventoryHandler {
    PlayerDAO playerDAO;
    ItemPackDAO itemPackDAO;
    UnitManager unitManager;
    Session session;

    public InventoryHandler(Session session) {
        playerDAO = new PlayerDAO(session);
        itemPackDAO = new ItemPackDAO(session);
        unitManager = new UnitManager(session);
        this.session = session;
    }

    public InventoryResultMessage handle(InventoryRequestMessage request, int player_id) {
        // get related object from database
        String action = request.getAction();
        InventoryResultMessage resultMessage = new InventoryResultMessage();
        Player player = playerDAO.getPlayer(player_id);
        int item_id = request.getItemPackID();
        ItemPack itemPack = itemPackDAO.getItemPack(item_id);
        Unit unit = unitManager.getUnit(request.getUnitID());
        try {
            if (action.equals("list")) {
                System.out.println("enter list");
                Thread.sleep(10000);
                resultMessage.setResult("valid");
            } else if (action.equals("use")) {
                if (validate(player, itemPack)) {
                    player.useItem(itemPack, 1, unit);
                }
                // remove itempack from database if doesn't belongs to the player
                if (itemPack.getPlayer() == null) {
                    session.delete(itemPack);
                }
                session.update(player);
                session.update(unit);
                resultMessage.setResult("valid");
            } else if (action.equals("drop")) {
                if (validate(player, itemPack)) {
                    player.dropItem(itemPack, 1);
                }
                // remove itempack from database if doesn't belongs to the player
                if (itemPack.getPlayer() == null) {
                    session.delete(itemPack);
                }
                session.update(player);
                resultMessage.setResult("valid");
            }
        } catch (Exception e) {
            resultMessage.setResult("invalid:" + e.getMessage());
        }

        AttributeRequestMessage attributeRequestMessage = new AttributeRequestMessage();
        resultMessage.setAttributeResultMessage((new AttributeHandler(session)).handle(attributeRequestMessage, player_id));
        resultMessage.setItems(player.getItems());
        resultMessage.setMoney(player.getMoney());

        return resultMessage;
    }

    public boolean validate(Player player, ItemPack itemPack) throws Exception {
        // check if player have item
        if (!player.checkItem(itemPack, 1)) {
            throw new Exception("Don't have enough item");
        }

        return true;
    }
}
