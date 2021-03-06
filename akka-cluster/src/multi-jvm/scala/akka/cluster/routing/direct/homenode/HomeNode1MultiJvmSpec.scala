package akka.cluster.routing.direct.homenode

import akka.config.Config
import akka.actor.Actor
import akka.cluster.{ ClusterTestNode, MasterClusterTestNode, Cluster }
import Cluster._
import akka.cluster.LocalCluster._

object HomeNodeMultiJvmSpec {

  val NrOfNodes = 2

  class SomeActor extends Actor with Serializable {
    def receive = {
      case "identify" ⇒ {
        reply(Config.nodename)
      }
    }
  }

}

class HomeNodeMultiJvmNode1 extends MasterClusterTestNode {

  import HomeNodeMultiJvmSpec._

  val testNodes = NrOfNodes

  "___" must {
    "___" in {
      Cluster.node.start()
      barrier("waiting-for-begin", NrOfNodes).await()
      barrier("waiting-for-end", NrOfNodes).await()
      node.shutdown()
    }
  }
}

class HomeNodeMultiJvmNode2 extends ClusterTestNode {

  import HomeNodeMultiJvmSpec._

  "Direct Router: A Direct Router" must {
    "obey 'home-node' config option when instantiated actor in cluster" in {
      Cluster.node.start()
      barrier("waiting-for-begin", NrOfNodes).await()

      val actorNode1 = Actor.actorOf(Props[SomeActor]("service-node1")
      val name1 = (actorNode1 ? "identify").get.asInstanceOf[String]
      name1 must equal("node1")

      val actorNode2 = Actor.actorOf(Props[SomeActor]("service-node2")
      val name2 = (actorNode2 ? "identify").get.asInstanceOf[String]
      name2 must equal("node2")

      barrier("waiting-for-end", NrOfNodes).await()
      node.shutdown()
    }
  }
}
