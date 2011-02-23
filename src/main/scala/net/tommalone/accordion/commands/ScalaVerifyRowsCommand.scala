package net.tommalone.accordion.commands

import org.concordion.api._
import java.util.regex.Pattern
import org.concordion.internal.util.Check
import org.concordion.internal.{Row, TableSupport}

class ScalaVerifyRowsCommand extends BaseWithVerifyRowsListenerCommand {

  override def verify(commandCall: CommandCall, evaluator: Evaluator, resultRecorder: ResultRecorder): Unit = {
    val pattern = Pattern.compile("(#.+?) *: *(.+)")
    val matcher = pattern.matcher(commandCall.getExpression())
    if (!matcher.matches()) {
      throw new RuntimeException("The expression for a \"verifyRows\" should be of the form: #var : collectionExpr")
    }
    val loopVariableName = matcher.group(1)
    val iterableExpression = matcher.group(2)

    val obj = evaluator.evaluate(iterableExpression)
    Check.notNull(obj, "Expression returned null (should be an Iterable).")
    Check.isTrue(obj.isInstanceOf[Iterable[_]], obj.getClass().getCanonicalName() + " is not Iterable")
    val iterable = obj.asInstanceOf[Iterable[Object]]

    val tableSupport = new TableSupport(commandCall)
    val detailRows = tableSupport.getDetailRows()

    announceExpressionEvaluated(commandCall.getElement())

    var index = 0
    for (loopVar <- iterable) {
      evaluator.setVariable(loopVariableName, loopVar)
      var detailRow: Row = null
      if (detailRows.length > index) {
        detailRow = detailRows(index)
      } else {
        detailRow = tableSupport.addDetailRow()
        announceSurplusRow(detailRow.getElement())
      }
      tableSupport.copyCommandCallsTo(detailRow)
      commandCall.getChildren().verify(evaluator, resultRecorder)
      index += 1
    }

    for (j <- index.until(detailRows.length)) {
      val detailRow = detailRows(j)
      resultRecorder.record(Result.FAILURE)
      announceMissingRow(detailRow.getElement())
    }
  }

}