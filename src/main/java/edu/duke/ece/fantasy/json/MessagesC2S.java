package edu.duke.ece.fantasy.json;

public class MessagesC2S {
    private LoginRequestMessage loginRequestMessage;
    private SignUpRequestMessage signUpRequestMessage;
    private PositionRequestMessage positionRequestMessage;
    private BattleRequestMessage battleRequestMessage;
    private ShopRequestMessage shopRequestMessage;
    private AttributeRequestMessage attributeRequestMessage;

    public MessagesC2S(){ }

    public MessagesC2S(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public MessagesC2S(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public MessagesC2S(PositionRequestMessage positionRequestMessage) { this.positionRequestMessage = positionRequestMessage; }

    public MessagesC2S(BattleRequestMessage battleRequestMessage) { this.battleRequestMessage = battleRequestMessage; }

    public LoginRequestMessage getLoginRequestMessage() {
        return loginRequestMessage;
    }

    public void setLoginRequestMessage(LoginRequestMessage loginRequestMessage) {
        this.loginRequestMessage = loginRequestMessage;
    }

    public SignUpRequestMessage getSignUpRequestMessage() {
        return signUpRequestMessage;
    }

    public void setSignUpRequestMessage(SignUpRequestMessage signUpRequestMessage) {
        this.signUpRequestMessage = signUpRequestMessage;
    }

    public PositionRequestMessage getPositionRequestMessage() {
        return positionRequestMessage;
    }

    public void setPositionRequestMessage(PositionRequestMessage positionRequestMessage) {
        this.positionRequestMessage = positionRequestMessage;
    }

    public BattleRequestMessage getBattleRequestMessage() {
        return battleRequestMessage;
    }

    public void setBattleRequestMessage(BattleRequestMessage battleRequestMessage) {
        this.battleRequestMessage = battleRequestMessage;
    }

    public AttributeRequestMessage getAttributeRequestMessage() { return attributeRequestMessage; }

    public void setAttributeRequestMessage(AttributeRequestMessage attributeRequestMessage) {
        this.attributeRequestMessage = attributeRequestMessage;
    }

    public ShopRequestMessage getShopRequestMessage() {
        return shopRequestMessage;
    }

    public void setShopRequestMessage(ShopRequestMessage shopRequestMessage) {
        this.shopRequestMessage = shopRequestMessage;
    }
}
