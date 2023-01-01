package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.model.entity.Image;
import ra.model.repository.ImageRepository;
import ra.model.service.ImageService;

import java.util.List;

@Service
public class ImageServiceImp implements ImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    @Override
    public Image findById(int imageId) {
        return imageRepository.findById(imageId).get();
    }

    @Override
    public Image saveOfUpdate(Image image) {
        return imageRepository.save(image);
    }

    @Override
    public void delete(int imageId) {
        imageRepository.deleteById(imageId);
    }

    @Override
    public List<Image> searchImageByProductId(int productId) {
        return imageRepository.findByProduct_ProductId(productId);
    }
}
