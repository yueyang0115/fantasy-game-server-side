package edu.duke.ece.fantasy;

import edu.duke.ece.fantasy.database.DAO.MetaDAO;
import edu.duke.ece.fantasy.database.DAO.PlayerDAO;
import edu.duke.ece.fantasy.database.DAO.RelationshipDAO;
import edu.duke.ece.fantasy.database.Player;
import edu.duke.ece.fantasy.database.Relationship;
import edu.duke.ece.fantasy.dto.PlayerInfo;
import edu.duke.ece.fantasy.json.FriendRequestMessage;
import edu.duke.ece.fantasy.json.FriendResultMessage;

import java.util.ArrayList;
import java.util.List;

public class FriendHandler {
    private PlayerDAO playerDAO;
    private RelationshipDAO relationshipDAO;

    public FriendHandler(MetaDAO metaDAO) {
        this.playerDAO = metaDAO.getPlayerDAO();
        relationshipDAO = metaDAO.getRelationshipDAO();
    }

    public FriendResultMessage handle(int playerId, FriendRequestMessage friendRequestMessage) {
        FriendResultMessage res = new FriendResultMessage();
        FriendRequestMessage.ActionType actionType = friendRequestMessage.getAction();
        if (actionType == FriendRequestMessage.ActionType.search) {
            // search user by username and get user info
            List<PlayerInfo> playerInfoList = new ArrayList<>();
            Player selectedPlayer = playerDAO.getPlayer(friendRequestMessage.getUsername());
            if (selectedPlayer != null && playerId != selectedPlayer.getId()) {
                PlayerInfo playerInfo = new PlayerInfo(selectedPlayer);
                playerInfoList.add(playerInfo);
            }
            res.setPlayerInfoList(playerInfoList);
        } else if (actionType == FriendRequestMessage.ActionType.apply) {
            if(!relationshipDAO.checkFriend(playerId,friendRequestMessage.getId())) {
                relationshipDAO.applyFriend(playerId, friendRequestMessage.getId());
            }
        } else if (actionType == FriendRequestMessage.ActionType.check) {
            List<Player> playerList = relationshipDAO.getApprovedFriendList(playerId);
            List<PlayerInfo> playerInfos = new ArrayList<>();
            for (Player p : playerList) {
                playerInfos.add(new PlayerInfo(p));
            }
            res.setPlayerInfoList(playerInfos);
        }
        return res;
    }
}