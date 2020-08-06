package edu.duke.ece.fantasy.Building;

import edu.duke.ece.fantasy.Annotation.Controller;
import edu.duke.ece.fantasy.Annotation.RequestMapping;
import edu.duke.ece.fantasy.Building.Message.ShopRequestMessage;
import edu.duke.ece.fantasy.GameContext;
import edu.duke.ece.fantasy.net.UserSession;

@Controller
public class ShopController {
    @RequestMapping
    public void handleShopReq(UserSession session, ShopRequestMessage msg) {
        GameContext.getShopHandler().handle(session, msg);
    }
}
