package com.company;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;


public class Main {
    static User user;
    static HashMap<String, User> map = new HashMap<>();

    public static void main(String[] args) {
            Spark.init();
            Spark.get(
                    "/",
                    (request, response) -> {
                        HashMap m = new HashMap();
                        if (user == null) {
                            return new ModelAndView(m, "index.html");
                        }
                        else {
                            m.put("name", user.name);
                            m.put("message", user.arrayList);
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
                        user = map.get(username);
                        if (user == null) {
                            user = new User(username, password);
                            map.put(username, user);
                        }
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
                    user = null;
                    response.redirect("/");
                    return "";
                }
            );

    }
}
