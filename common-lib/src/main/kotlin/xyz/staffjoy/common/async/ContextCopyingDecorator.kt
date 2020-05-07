package xyz.staffjoy.common.async

import org.springframework.core.task.TaskDecorator
import org.springframework.web.context.request.RequestContextHolder

// https://stackoverflow.com/questions/23732089/how-to-enable-request-scope-in-async-task-executor
class ContextCopyingDecorator : TaskDecorator {
    override fun decorate(runnable: Runnable): Runnable {
        val context = RequestContextHolder.currentRequestAttributes()
        return Runnable {
            try {
                RequestContextHolder.setRequestAttributes(context)
                runnable.run()
            } finally {
                RequestContextHolder.resetRequestAttributes()
            }
        }
    }
}