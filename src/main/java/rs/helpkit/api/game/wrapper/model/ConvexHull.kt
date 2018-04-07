package rs.helpkit.api.game.wrapper.model

import java.awt.*
import java.util.Stack

/**
 * @author Tyler Sedlar
 * @since 2/5/2016
 */
object ConvexHull {

    fun hullFor(points: MutableList<Point>): Hull? {
        if (points.size < 3 || !sort(points)) {
            return null
        }
        val stack = Stack<Point>()
        stack.push(points[0])
        stack.push(points[1])
        var i = 2
        while (i < points.size) {
            val head = points[i]
            val middle = stack.pop()
            val tail = stack.peek()
            val turn = resolveTurn(tail, middle, head)
            when (turn) {
                ConvexHull.Turn.COUNTER_CLOCKWISE -> {
                    stack.push(middle)
                    stack.push(head)
                }
                ConvexHull.Turn.CLOCKWISE -> {
                    i--
                }
                ConvexHull.Turn.COLLINEAR -> {
                    stack.push(head)
                }
            }
            i++
        }
        stack.push(points[0])
        val hull = Hull()
        stack.forEach { p -> hull.addPoint(p.x, p.y) }
        return hull
    }

    private fun resolveTurn(a: Point, b: Point, c: Point): Turn {
        val crossProduct = (b.x.toLong() - a.x) * (c.y.toLong() - a.y) - (b.y.toLong() - a.y) * (c.x.toLong() - a.x)
        return if (crossProduct > 0) {
            Turn.COUNTER_CLOCKWISE
        } else if (crossProduct < 0) {
            Turn.CLOCKWISE
        } else {
            Turn.COLLINEAR
        }
    }

    private fun sort(points: MutableList<Point>): Boolean {
        val lowest = lowest(points) ?: return false
        points.sortWith(Comparator({ a, b ->
            if (a == b || a == b) {
                return@Comparator 0
            }
            // use longs to guard against int-underflow
            val thetaA = Math.atan2((a.y.toLong() - lowest.y).toDouble(), (a.x.toLong() - lowest.x).toDouble())
            val thetaB = Math.atan2((b.y.toLong() - lowest.y).toDouble(), (b.x.toLong() - lowest.x).toDouble())
            if (thetaA < thetaB) {
                return@Comparator - 1
            } else if (thetaA > thetaB) {
                return@Comparator 1
            } else {
                // collinear with the 'lowest' point, let the point closest to it come first
                // use longs to guard against int-over/underflow
                val distanceA = Math.sqrt(((lowest.x.toLong() - a.x) * (lowest.x.toLong() - a.x) +
                        (lowest.y.toLong() - a.y) * (lowest.y.toLong() - a.y)).toDouble())
                val distanceB = Math.sqrt(((lowest.x.toLong() - b.x) * (lowest.x.toLong() - b.x) +
                        (lowest.y.toLong() - b.y) * (lowest.y.toLong() - b.y)).toDouble())
                if (distanceA < distanceB) {
                    return@Comparator - 1
                } else {
                    return@Comparator 1
                }
            }
        }))
        return true
    }

    private fun lowest(points: List<Point>): Point? {
        if (points.isEmpty()) {
            return null
        }
        var lowest = points[0]
        for (temp in points) {
            if (temp.y < lowest.y || temp.y == lowest.y && temp.x < lowest.x) {
                lowest = temp
            }
        }
        return lowest
    }

    enum class Turn {
        CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR
    }
}
