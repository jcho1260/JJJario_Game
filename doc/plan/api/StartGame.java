class StartGame {
    void main() {
        GameWorld game = controller.constructGame(gameChoiceString);
        controller.beginGame(game);
    }
}