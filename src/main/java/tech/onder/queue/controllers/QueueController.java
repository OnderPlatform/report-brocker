package tech.onder.queue.controllers;


import play.mvc.Result;
import play.mvc.Results;
import tech.onder.queue.service.QueueBackupService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class QueueController {
    
    private QueueBackupService queueBackupService;
    
    @Inject
    public QueueController(QueueBackupService queueBackupService) {
        this.queueBackupService = queueBackupService;
    }
    
    public Result backup() {
        queueBackupService.backup().thenApply((v) -> Results.ok());
        return Results.ok();
    }
    
    public CompletableFuture<Result> restore() {
        return queueBackupService.restore().thenApply((v) -> Results.ok());
    }
    
}
