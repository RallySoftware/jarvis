(defproject jarvis "1.0.0"
  :description "A robot for FlowDock"
  :url "http://github.com/RallySoftware/jarvis"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.5.0"]
                 [com.rallydev/jarvis-core "1.0.0"]
                 [netty-ring-adapter "0.2.4"]
                 [ch.qos.logback/logback-classic "1.0.9"]
                 [compojure "1.1.3"]]
  :plugins [[lein-deploy-uberjar "2.0.0"]]
  :jvm-opts ["-Xmx1G" "-DLOG_DIR=./logs"]
  :main jarvis.core)
