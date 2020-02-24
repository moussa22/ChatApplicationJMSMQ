import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSChat extends Application {

    public static void main(String[] args){
        Application.launch(JMSChat.class);
    }

    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("JMS Chat");

        BorderPane borderPane=new BorderPane();

        HBox hBox=new HBox();
        hBox.setPadding(new Insets(10));
        hBox.setSpacing(10);
        hBox.setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY,Insets.EMPTY)));
        Label labelCode=new Label("Code:");
        final TextField textFieldCode=new TextField("C1");
        textFieldCode.setPromptText("Code");

        Label labelHost=new Label("Host:");
        final TextField textFieldHost=new TextField("localhost");
        textFieldHost.setPromptText("Host");

        Label labelPort=new Label("Port:");
        final TextField textFieldPort=new TextField("61616");
        textFieldPort.setPromptText("Port");

        Button button=new Button("Connexion");
        hBox.getChildren().add(labelCode);
        hBox.getChildren().add(textFieldCode);
        hBox.getChildren().add(labelHost);
        hBox.getChildren().add(textFieldHost);
        hBox.getChildren().add(labelPort);
        hBox.getChildren().add(textFieldPort);
        hBox.getChildren().add(button);
        borderPane.setTop(hBox);

        VBox vBox=new VBox();
        GridPane gridPane=new GridPane();
        HBox hBox2=new HBox();
        vBox.getChildren().add(gridPane);
        vBox.getChildren().add(hBox2);
        borderPane.setCenter(vBox);
        Label labelTo=new Label("To");
        TextField textFieldTo=new TextField("C1");
        textFieldTo.setPrefWidth(150);
        Label labelMessage=new Label("Message");
        TextArea textAreMessage=new TextArea();
        Button buttonSend=new Button("Send");
        textAreMessage.setPrefWidth(150);
        Label labelImage=new Label("Image");
        ComboBox<String> comboBoxImage=new ComboBox<String>();
        Button buttonSendImage=new Button("Send Image");
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        textAreMessage.setPrefRowCount(2);
        gridPane.add(labelTo,0,0); gridPane.add(textFieldTo,1,0);
        gridPane.add(labelMessage,0,1);
        gridPane.add(textAreMessage,1,1);
        gridPane.add(buttonSend,2,1);
        gridPane.add(labelImage,0,2);
        gridPane.add(comboBoxImage,1,2);
        gridPane.add(buttonSendImage,1,2);

        Scene scene=new Scene(borderPane,800,500);

        primaryStage.setScene(scene);
        primaryStage.show();
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {

                try {
                    String codeUser=textFieldCode.getText();
                    String host=textFieldHost.getText();
                    int port=Integer.parseInt(textFieldPort.getText());
                    ConnectionFactory connectionFactory=new ActiveMQConnectionFactory("tcp://"+host+":"+port);
                    Connection connection=connectionFactory.createConnection();
                    connection.start();
                    Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
                    Destination destination=session.createTopic("enset.chat");
/*
                    MessageConsumer messageConsumer=session.createConsumer(destination,"code='"+codeUser+"'");
*/
                    MessageConsumer messageConsumer=session.createConsumer(destination);

                    messageConsumer.setMessageListener(message->{


                                if(message instanceof TextMessage){
                                TextMessage textMessage= (TextMessage) message;

                                    try {
                                        System.out.println(((TextMessage) message).getText());
                                    } catch (JMSException e) {
                                        e.printStackTrace();
                                    }
                                } else if(message instanceof TextMessage){


                                    }



                    });
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

