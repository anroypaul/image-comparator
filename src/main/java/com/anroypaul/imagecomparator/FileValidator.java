package com.anroypaul.imagecomparator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator implements Validator {

    private static final String[] ALLOWED_CONTENT_TYPES = {"image/jpeg", "image/png"};

    @Override
    public boolean supports(Class<?> clazz) {
        return ImageForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ImageForm fileForm = (ImageForm) target;
        validateFile(fileForm.getFirstImage(), "firstImage", errors);
        validateFile(fileForm.getSecondImage(), "secondImage", errors);
    }

    private void validateFile(MultipartFile file, String fieldName, Errors errors) {
        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            System.out.println(file);
            System.out.println(contentType);
            if (contentType != null) {
                boolean isValidType = false;
                for (String allowedContentType : ALLOWED_CONTENT_TYPES) {
                    if (contentType.equals(allowedContentType)) {
                        isValidType = true;
                        break;
                    }
                }
                System.out.println(isValidType);
                if (!isValidType) {
                    errors.rejectValue(fieldName, "file.invalid.type", "Invalid file type");
                }
            }
        }
    }
}
