# Jarvis

A Flowdock robot similar to [Hubot](http://hubot.github.com/).

## Why not just use Hubot?

I think Jarvis can take advantage of the full power of Flowdock. Context. Think of all those build messages that get
sent to Flowdock. Wouldn't it be nice if you could reply to those messages and have Jarvis do work for you. Reply to
a failed build message in Flowdock with:

```
~claim portfolio_item/bulk_edit_spec.rb failed again
```

Notice that we didn't have to tell Jarvis which build to claim. Jarvis can infer that information from the context
of message command.

## How do I use Jarvis?

The Jarvis jar file includes a bot namespace which exposes an init function. This will spin up a new thread pool with a specified number of threads and connect to the flows that he has been invited to.

For example -
```clojure
(ns my-jarvis.core
    (:require [jarvis.bot :as jarvis]))

(defn -main []
  (jarvis/init))
```

## Environment Variables
```bash
export FLOWDOCK_TOKEN=<user flowdock token>
export THREAD_POOL_SIZE=<number of threads to give jarvis -- defaults to 100>
```
Your Flowdock token is located on the account page. https://www.flowdock.com/account/tokens

## How is Jarvis different from Hubot.

* Jarvis works on the JVM.
* Plugins can currently be created with Groovy, Java and Clojure. It should be easy to add support for other JVM languages too.
* Jarvis only works with Flowdock
* Jarvis is developed in a more functional style. (full Flowdock message) -> (response)
* Jarvis is designed to take full advantage of Flowdock context.
* Jarvis can be used in private flows.
* Jarvis can be invited to flows, and be asked to leave.

## How to discover available commands
Type ~help in a flow that Jarvis has been invited to. You will receive a reply along these lines, with examples of the required syntax for parameterized commands:
```
assign-team:	assign-team <username> <team> - Assigns the user to the GitHub team.
build:	Trigger build pipeline (which may cause a deployment) for specified service.
claim:	claim <message> - Claims a broken build. - This is broken until we get Jenkins authentication for Jarvis.
create-repo:	create-repo <repo> <team> - Creates a GitHub repo.
health-report:	Prints the health report for the specified job.
hello:	Simple Hello from Groovy
help:	Prints the description of all plugins.
join:	Join the flow that is specified.
last-successful-build:	Prints the last successful build for the specified job.
list-flows:	Prints all flows that the jarvis is listening for.
list-github-teams:	list-github-teams - Shows the list of all our Github teams.
status:	Determines the status of all downstream jobs from the specified job.
~leave can be used to have Jarvis leave a flow. He will need to be invited back through the flowdock api to join again.
~<command> can be used also in private-messages with Jarvis.
~~<command> @<name> will private message the people listed the results of the executed command.
```

## Example Plugin

```groovy
import com.rallydev.jarvis.Bot

Bot.addCommand("hello", "Simple Hello", "Adam Esterline") { message ->
    "Hello from Groovy"
}
```

## What information does a Flowdock message contain?

Flowdock has a pretty good [API](https://www.flowdock.com/api/). But... with any documentation, it is not perfect.
I have taken the time to "document" some of the API in the [wiki](https://github.com/RallySoftware/jarvis/wiki).

## Jarvis specific message information

Jarvis adds information to the basic Flowdock information. This information allows plugin developers to access
Flowdock "context" information. Two pieces of information have been altered: ```user, parent```.

### user

In the original ```user``` key, the user value was the user id of the sender of the message. Jarvis takes this an extra
step and adds all the user information.

```json
{ "app" : null,
  "attachments" : [  ],
  "content" : { "last_activity" : 1361748229643 },
  "event" : "activity.user",
  "flow" : "rally-software:the-fellowship",
  "id" : 904125,
  "parent" : null,
  "sent" : 1361748227604,
  "tags" : [  ],
  "user" : { "avatar" : "https://d2cxspbh1aoie1.cloudfront.net/avatars/3a5f36a2e4e283537ee9e037d9390dbd/",
      "email" : "aesterline@rallydev.com",
      "id" : 29990,
      "name" : "Adam Esterline",
      "nick" : "Adam"
    },
  "uuid" : null
}
```

### parent

If the current message is a comment to a message, Jarvis will add a ```parent``` key to the message map. This key
will contain the original message for the comment.

```json
{ "app" : "chat",
  "attachments" : [  ],
  "content" : { "text" : "$example",
      "title" : "Everything operating normally."
    },
  "event" : "comment",
  "flow" : "rally-software:the-fellowship",
  "id" : 904196,
  "parent" : { "app" : "influx",
      "attachments" : [  ],
      "content" : { "contributors" : null,
          "coordinates" : null,
          "created_at" : "Fri Feb 22 20:41:43 +0000 2013",
          "entities" : { "hashtags" : [  ],
              "urls" : [  ],
              "user_mentions" : [  ]
            },
          "favorited" : false,
          "filter_level" : "medium",
          "geo" : null,
          "id" : 305054788356280320,
          "id_str" : "305054788356280320",
          "in_reply_to_screen_name" : null,
          "in_reply_to_status_id" : null,
          "in_reply_to_status_id_str" : null,
          "in_reply_to_user_id" : null,
          "in_reply_to_user_id_str" : null,
          "place" : null,
          "retweet_count" : 0,
          "retweeted" : false,
          "source" : "<a href=\"https://status.github.com/\" rel=\"nofollow\">OctoStatus Production</a>",
          "text" : "Everything operating normally.",
          "truncated" : false,
          "user" : { "contributors_enabled" : false,
              "created_at" : "Tue Aug 28 00:04:59 +0000 2012",
              "default_profile" : true,
              "default_profile_image" : false,
              "description" : null,
              "favourites_count" : 0,
              "follow_request_sent" : null,
              "followers_count" : 1305,
              "following" : null,
              "friends_count" : 1,
              "geo_enabled" : false,
              "id" : 785764172,
              "id_str" : "785764172",
              "is_translator" : false,
              "lang" : "en",
              "listed_count" : 43,
              "location" : "",
              "name" : "GitHub Status",
              "notifications" : null,
              "profile_background_color" : "C0DEED",
              "profile_background_image_url" : "http://a0.twimg.com/images/themes/theme1/bg.png",
              "profile_background_image_url_https" : "https://si0.twimg.com/images/themes/theme1/bg.png",
              "profile_background_tile" : false,
              "profile_image_url" : "http://a0.twimg.com/profile_images/2577880769/687474703a2f2f636c2e6c792f696d6167652f337330463237324b3254324c2f636f6e74656e74_normal.png",
              "profile_image_url_https" : "https://si0.twimg.com/profile_images/2577880769/687474703a2f2f636c2e6c792f696d6167652f337330463237324b3254324c2f636f6e74656e74_normal.png",
              "profile_link_color" : "0084B4",
              "profile_sidebar_border_color" : "C0DEED",
              "profile_sidebar_fill_color" : "DDEEF6",
              "profile_text_color" : "333333",
              "profile_use_background_image" : true,
              "protected" : false,
              "screen_name" : "githubstatus",
              "statuses_count" : 131,
              "time_zone" : "Pacific Time (US & Canada)",
              "url" : "http://status.github.com/",
              "utc_offset" : -28800,
              "verified" : false
            }
        },
      "edited" : null,
      "event" : "twitter",
      "flow" : "rally-software:the-fellowship",
      "id" : 848344,
      "sent" : 1361565704184,
      "tags" : [ ":thread" ],
      "user" : 0
    },
  "sent" : 1361748434205,
  "tags" : [ "influx:848344" ],
  "user" : { "avatar" : "https://d2cxspbh1aoie1.cloudfront.net/avatars/3a5f36a2e4e283537ee9e037d9390dbd/",
      "email" : "aesterline@rallydev.com",
      "id" : 29990,
      "name" : "Adam Esterline",
      "nick" : "Adam"
    },
  "uuid" : "Lq4SLeiil4NXb5ud"
}
```


## How can I test my plugin?

Take a look at the tests currently written for the
[existing](https://github.com/RallySoftware/jarvis-core/tree/master/src/test/clojure/jarvis/plugins)
[plugins](https://github.com/RallySoftware/jarvis-core/blob/master/src/test/clojure/jarvis/plugins/help_test.clj).

## How do I try out my plugin?

The Jarvis core is written in Clojure, so you will need to install
[Leiningen] (https://github.com/technomancy/leiningen#installation). Once you have install Leiningen, run the below
commands to start Jarvis and try out your plugin.

```
> lein run
```

## TODO
* Add support for JavaScript/CoffeeScript plugins
* ~~Add a better way to start Jarvis. Maybe just a script at the root of the project.~~

## Why the name Jarvis?

See [http://ironman.wikia.com/wiki/J.A.R.V.I.S.](http://ironman.wikia.com/wiki/J.A.R.V.I.S.)

## Dependencies
```clojure
[org.clojure/clojure "1.5.0"]
[com.rallydev/jarvis-core "1.0.0"]
[netty-ring-adapter "0.2.4"]
[ch.qos.logback/logback-classic "1.0.9"]
[compojure "1.1.3"]
```

# License
Copyright (c) Rally Software Development Corp. 2013  
Distributed under the MIT License.
