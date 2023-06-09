module model.maze2d {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
           // requires eu.hansolo.tilesfx;
            requires com.almasb.fxgl.all;
    requires annotations;

    exports model.maze.AStarAlgorithm;
    opens model.maze.AStarAlgorithm to javafx.fxml;
    exports model.maze.MazeDependencies;
    opens model.maze.MazeDependencies to javafx.fxml;

    exports model.maze.Controller;
    opens model.maze.Controller to javafx.fxml;
    exports model.maze;
    opens model.maze to javafx.fxml;
    //opens model.maze2d.Testing to javafx.fxml;
}