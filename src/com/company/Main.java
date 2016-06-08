package com.company;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;


public class Main {
    static User user;
    static HashMap<String, User> map = new HashMap<>();

    /** EXAMPLES OF MUSTACHE
     * ArrayList<String> {{#names}} {{.}} -> pulls the whole field {{/name}}
     * ArrayList<Message> msgs {{#msgs}} {{text}} {{/msgs}}
     * Conditional {{#name}} Hi{{.}} {{/name}}
     * Conditional {{name}} Not Logged In! {{/name}}
     */

    public static void main(String[] args) {
            Spark.staticFileLocation("public");
            Spark.init();
            Spark.get(
                    "/",
                    (request, response) -> {
                        Session session = request.session();
                        String username = session.attribute("username");

                        HashMap m = new HashMap();
                        if (username == null) {
                            return new ModelAndView(m, "index.html");
                        }
                        else {
                            User user = map.get(username);
                            for (int i = 0; i < user.arrayList.size(); i++) {
                                user.arrayList.get(i).id = i;
                                System.out.println();
                            }
                            m.put("message", user.arrayList);
                            m.put("name", username);
                            return new ModelAndView(m, "message.html");
                        }
                    },
                    new MustacheTemplateEngine()
            );
            Spark.post(
                    "/create-user",
                    (request, response) -> {
                        String username = request.queryParams("username");
                        String password = request.queryParams("password");
                        if (username == null || password == null) {
                            throw new Exception("Name or pass not sent");
                        }
                        user = map.get(username);
                        if (user == null) {
                            user = new User(username, password);
                            map.put(username, user);
                        }
                        else if (!password.equals(user.password)) {
                            throw new Exception("Wrong password");
                        }

                        Session session = request.session();
                        session.attribute("username", username);
                        response.redirect("/");
                        return "";
                    }
            );
            Spark.post(
                    "/create-message",
                    (request, response) -> {
                    String createMessage = request.queryParams("createMessage");
                    Message message = new Message(createMessage);
                    user.arrayList.add(message);
                    response.redirect("/");
                    return "";
                    }
            );
            Spark.post(
                "/logout",
                (request, response) -> {
                    Session session = request.session();
                    session.invalidate();
                    response.redirect("/");
                    return "";
                }
            );
            Spark.post(
                "/delete-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }

                    int id = Integer.valueOf(request.queryParams("id"));

                    User user = map.get(username);
                    user.arrayList.remove(id);

                    response.redirect("/");
                    return "";
                }
            );
            Spark.post(
                "/edit-message",
                (request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    if (username == null) {
                        throw new Exception("Not logged in");
                    }

                    int num = Integer.valueOf(request.queryParams("num"));

                    User user = map.get(username);
                    if (num <= 0 || num - 1 >= user.arrayList.size()) {
                        throw new Exception("Invalid number");
                    }
                    String edit = request.queryParams("edit");

                    Message editMessage = user.arrayList.get(num - 1);
                    editMessage.message = edit;

                    response.redirect("/");
                    return "";
                }
            );
    }
}
