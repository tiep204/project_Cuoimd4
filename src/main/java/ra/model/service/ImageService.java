package ra.model.service;

import ra.model.entity.Image;

import java.util.List;

public interface ImageService {
    List<Image> findAll();
    Image findById(int imageId);
    Image saveOfUpdate(Image image);
    void delete(int imageId);
    List<Image> searchImageByProductId(int productId);
}
