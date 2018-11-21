package tech.onder.core.repositories;


import tech.onder.core.models.Meter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MeterRepo extends AbstractInMemoryRepo<String, Meter> {

    private final Map<String, Meter> meters;

    @Inject
    public MeterRepo() {
        Map<String, Meter> map = new HashMap<>();

        map.put("0x85176b816BEd182c08DdaAcCF6fD060a152Da175",new Meter("0x85176b816BEd182c08DdaAcCF6fD060a152Da175", ""));
        map.put("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", new Meter("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", "QF1"));
        map.put("0x44ac47379573F997098d02714D652abc28629c44", new Meter("0x44ac47379573F997098d02714D652abc28629c44", "QF4"));
        map.put("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", new Meter("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", "QF7"));


        map.put("0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3",new Meter("0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3", ""));
        map.put("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", new Meter("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", "QF2"));
        map.put("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", new Meter("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", "QF5"));

        map.put("0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5",new Meter("0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5", ""));
        map.put("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", new Meter("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", "QF3"));
        map.put("0x87e080151c23E6bB0ccCABFf0C0162d717a4c865", new Meter("0x87e080151c23E6bB0ccCABFf0C0162d717a4c865", "QF6"));

        this.meters = map;
    }

    @Override
    public Map<String, Meter> getValues() {
        return this.meters;
    }
}
