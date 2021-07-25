# Dapenbi Read Module

## Developing App Read Module
Merupakan aplikasi terschedule yang membaca sebuah file csv dan memvalidasi data, dimana jika sukses akan tergenerate <nip>.txt dan akan mengirimkan sms sesuai gendernya masing-masing

## Server information
- OS : windows 10
- spring boot versi 2.5.3
- Maven 3.8.1
 

## Code information
- konfigurasi koneksi ke database , folder csv, folder txt terdapat di (src/main/resources/application.properties)

## How to use
- Compile : mvn clean package
- Run : mvn spring-boot:run
