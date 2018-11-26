package tech.onder.meters.repositories;

import tech.onder.meters.models.BuyerSeller;
import tech.onder.meters.models.Meter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
@Singleton
public class MeterRelationRepo extends AbstractInMemoryRepo<String, BuyerSeller> {

    private final Map<String, BuyerSeller> values;
@Inject
    public MeterRelationRepo() {
        Map<String, BuyerSeller> relMap = new HashMap<>();
        relMap.put("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", new BuyerSeller("0x2E9b32581820f613B4B9b7B67308639AaB8C6Ff7", "0x85176b816BEd182c08DdaAcCF6fD060a152Da175"));
        relMap.put("0x44ac47379573F997098d02714D652abc28629c44", new BuyerSeller("0x44ac47379573F997098d02714D652abc28629c44", "0x85176b816BEd182c08DdaAcCF6fD060a152Da175"));
        relMap.put("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", new BuyerSeller("0xcb32de2b9d1f1efb4abde7d24131ebed6c649ad7", "0x85176b816BEd182c08DdaAcCF6fD060a152Da175"));

        relMap.put("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", new BuyerSeller("0xF9F12156CD1F325c508AF6a861F9f488Cab5a37c", "0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3"));
        relMap.put("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", new BuyerSeller("0xF6EBc1744a067F826D687B366a3CE7d7BD7af07F", "0xf3718908E40B4FCd3ec8656b152C2C67D3357DC3"));

    relMap.put("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", new BuyerSeller("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", "0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5"));
    relMap.put("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", new BuyerSeller("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", "0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5"));
    relMap.put("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", new BuyerSeller("0x05407aE7c6457b73B231B4AbDe7ae4C07472e264", "0xA5cfA47Ae9E546BC7A771a7AC6fc428b4BC6cdE5"));


    this.values = relMap;
    }

    @Override
    public Map<String, BuyerSeller> getValues() {
        return values;
    }



    public void save(BuyerSeller model) {
        this.values.put(model.getTo(), model);
    }
}
