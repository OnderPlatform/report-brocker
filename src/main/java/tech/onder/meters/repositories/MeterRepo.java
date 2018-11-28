package tech.onder.meters.repositories;


import tech.onder.meters.models.Meter;

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

        map.put("0x85176b816BEd182c08DdaAcCF6fD060a152Da175", new Meter("0x85176b816BEd182c08DdaAcCF6fD060a152Da175", "Фаза А"));
        map.put("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", new Meter("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", "QF1"));
        map.put("0x44ac47379573F997098d02714D652abc28629c44", new Meter("0x44ac47379573F997098d02714D652abc28629c44", "QF4"));
        map.put("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", new Meter("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", "QF7"));
        map.put("0xb1e14037a0C35B81A2ED086D98e4aCe0A5229c96", new Meter("0xb1e14037a0C35B81A2ED086D98e4aCe0A5229c96", "QF10"));
        map.put("0x74EcbE56958969A0b166DC95Cc0F4Aa6aA035fD7", new Meter("0x74EcbE56958969A0b166DC95Cc0F4Aa6aA035fD7", "QF13"));
        map.put("0xCd8591eAF4Ae443280119a2003bfdf4B125f1886", new Meter("0xCd8591eAF4Ae443280119a2003bfdf4B125f1886", "QF15"));


        map.put("0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3", new Meter("0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3", "Фаза B"));
        map.put("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", new Meter("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", "QF2"));
        map.put("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", new Meter("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", "QF5"));
        map.put("0x5D64FE69B13B6A847d240c3BA558485De90c62b8", new Meter("0x5D64FE69B13B6A847d240c3BA558485De90c62b8", "QF8"));
        map.put("0x85Bb914712eeCe6eCcdeCC0dc2e380e367a96f14", new Meter("0x85Bb914712eeCe6eCcdeCC0dc2e380e367a96f14", "QF11"));
        map.put("0x9e02ea42Dcc44e15357De7600f0700545b78B4b8", new Meter("0x9e02ea42Dcc44e15357De7600f0700545b78B4b8", "QF14"));
        map.put("0x2334302F71D5Eb13c8E769e3Aa35152c898c3eD8", new Meter("0x2334302F71D5Eb13c8E769e3Aa35152c898c3eD8", "Уборочные розетки"));


        map.put("0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5", new Meter("0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5", "Фаза C"));
        map.put("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", new Meter("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", "QF3"));
        map.put("0x87e080151c23E6bB0ccCABFf0C0162d717a4c865", new Meter("0x87e080151c23E6bB0ccCABFf0C0162d717a4c865", "QF6"));
        map.put("0x68f0F4cc61a81E58D1d73209AD62cB34EC2CA47C", new Meter("0x68f0F4cc61a81E58D1d73209AD62cB34EC2CA47C", "QF9"));
        map.put("0xFE69B7F694B5a62Ce377660b976CAeC867a995CC", new Meter("0xFE69B7F694B5a62Ce377660b976CAeC867a995CC", "QF12"));
        map.put("0x2533BA9A4C83dCD4f6D54a6b70E38F28362489C6", new Meter("0x2533BA9A4C83dCD4f6D54a6b70E38F28362489C6", "QF16"));

        this.meters = map;
    }

    @Override
    public Map<String, Meter> getValues() {
        return this.meters;
    }

    public void save(Meter model) {
        this.meters.put(model.getUuid(), model);
    }
}
