package edu.duke.ece.fantasy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece.fantasy.Item.Item;
import edu.duke.ece.fantasy.database.*;
import edu.duke.ece.fantasy.database.DAO.DBBuildingDAO;
import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.ShopInventoryDAO;
import edu.duke.ece.fantasy.json.ShopRequestMessage;
import edu.duke.ece.fantasy.json.ShopResultMessage;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//
class ShopHandlerTest {
    ShopHandler shopHandler;
    PlayerDAO playerDAO;
    ObjectMapper objectMapper = new ObjectMapper();
    Logger logger = LoggerFactory.getLogger(ShopHandlerTest.class);
    edu.duke.ece.fantasy.database.DAO.DBBuildingDAO DBBuildingDAO;
    ShopInventoryDAO shopInventoryDAO;
    Session session;
    WorldCoord shopCoord;

    public ShopHandlerTest() {
        session = HibernateUtil.getSessionFactory().openSession();
        MetaDAO metaDAO = new MetaDAO(session);
        shopHandler = new ShopHandler(metaDAO);
        DBBuildingDAO = metaDAO.getDbBuildingDAO();
        playerDAO = metaDAO.getPlayerDAO();
        shopInventoryDAO = metaDAO.getShopInventoryDAO();
    }


    @BeforeEach
    void start() {
        session.beginTransaction();
        Initializer initializer = new Initializer(session);
        initializer.initialize_test_player();
        shopCoord = initializer.initialize_test_shop();
    }

    @AfterEach
    void shutdown(){
        session.getTransaction().rollback();
    }

    @Test
    void handle_list() {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("list");

        Player player = playerDAO.getPlayer("test");
        ShopResultMessage resultMessage = shopHandler.handle(shopRequestMessage, player.getId());
        try {
            logger.info(objectMapper.writeValueAsString(resultMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void handle_buy() {
//        List<shopInventory> itemPacks = new ArrayList<>(DBShop.getItems());
        List<shopInventory> itemPacks = shopInventoryDAO.getInventories(shopCoord);

        for (int i = 0; i < itemPacks.size(); i++) {
            try {
                shopInventory select_item = itemPacks.get(i);
                Item item_obj = select_item.getDBItem().toGameItem();
                int itemPack_id = select_item.getId();
                int item_amount = select_item.getAmount();
                int required_money = item_obj.getCost() * item_amount;
                Player player = playerDAO.getPlayer("test");

                // Don't have enough money
                player.setMoney(required_money - 1);
                ShopResultMessage resultMessage = buy_item(player, itemPack_id, item_amount);
                assertEquals(item_amount, resultMessage.getItems().get(0).getAmount());
                assertNotEquals("valid", resultMessage.getResult());

                // shop don't have enough item
                player.setMoney(required_money);
                buy_item(player, itemPack_id, item_amount + 1);
                assertEquals(item_amount, resultMessage.getItems().get(0).getAmount());
                assertNotEquals("valid", resultMessage.getResult());

                // success
                player.setMoney(required_money);
                resultMessage = buy_item(player, itemPack_id, item_amount - 1);
                assertEquals("valid", resultMessage.getResult());
                try {
                    logger.info(objectMapper.writeValueAsString(resultMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                // success buy again
                player.setMoney(required_money);
                resultMessage = buy_item(player, itemPack_id, 1);

                assertEquals("valid", resultMessage.getResult());
                try {
                    logger.info(objectMapper.writeValueAsString(resultMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    ShopResultMessage buy_item(Player player, int inventory_id, int item_amount) {
        ShopRequestMessage shopRequestMessage = new ShopRequestMessage();
        shopRequestMessage.setCoord(shopCoord);
        shopRequestMessage.setAction("buy");
        Map<Integer, Integer> itemMap = new HashMap<>();
        itemMap.put(inventory_id, item_amount);
        shopRequestMessage.setItemMap(itemMap);
        return shopHandler.handle(shopRequestMessage, player.getId());
    }
}