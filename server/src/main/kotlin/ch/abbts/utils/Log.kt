package ch.abbts.utils

import org.slf4j.LoggerFactory
import org.slf4j.Logger

// see 2. approach here: https://gist.github.com/maxixcom/cd454e4b75730c17a515e8db15a4ae78

abstract class Log {
    val log: Logger = LoggerFactory.getLogger(this.javaClass)
}
