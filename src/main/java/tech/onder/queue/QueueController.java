package tech.onder.queue;

import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

public class QueueController {

    private QueueService queueService;

    @Inject
    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    public Result backup() {
        queueService.backup();
        return Results.ok();
    }

    public Result restore() {
        queueService.restore();
        return Results.ok();
    }
}
