(ns jarvis.core
  (:require [jarvis.bot :as jarvis]
            [netty.ring.adapter :as netty]
            [clojure.tools.logging :as log])
  (:use [compojure.core :exclude [routes]]
        [compojure.route])
  (:gen-class ))

(defroutes routes
  (GET "/metrics/healthcheck" [] {:content-type "text/html" :status 200})
  (not-found "Page not found"))

(defn -main []
  (jarvis/init)
  (log/info "Starting netty for health check")
  (netty/start-server routes {:port 8005}))
