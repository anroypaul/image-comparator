# image-comparator

### Description

This tool compares two images, highlights the differences and generates the new image with highlighted differences between two original images.
Images should be same dimensions. The comparison process is performed by using all Java built-in features and libraries.

**How to run:**
1. ```docker build --tag=image-comparator:latest .```
2. ```docker run -p8887:8080 image-comparator:latest```

**How to deploy:**
```TODO...```

**_TODO List:_**
- [x] Add file name when selected
- [x] Error message when only one file is trying to be uploaded
- [x] Display result image 
- [x] Add 'clear/reset' button that removes all images from memory and refreshes the page
- [x] File upload logic
- [x] Docker support
- [x] Check/restrict file extensions to upload
- [ ] Deploy it 

### Technologies used:
- Maven
- Spring Boot
- Thymeleaf
- TailwindCSS
- Docker