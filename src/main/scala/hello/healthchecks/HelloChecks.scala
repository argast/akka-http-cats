package hello.healthchecks

import com.timeout.healthchecks.HealthChecks

object HelloChecks {

  val all = HealthChecks.ping("www.google.com", 80)
}
