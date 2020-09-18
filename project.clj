(defproject clojure-demo "0.1.0-SNAPSHOT"
  :description "Learning Clojure I hold here various exercises"
  :url "https://github.com/fscotto/clojure-demo"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot clojure-demo.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :plugins [[lein-cljfmt "0.7.0"]])
