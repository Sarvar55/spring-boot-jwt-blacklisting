## JWT Blacklisting Nedir?

Blacklisting mekanizması, JWT'nin (JSON Web Token) geçerliliğini sonlandırmak için kullanılan bir yöntemdir. 
Temel olarak, blacklisting yetkilendirme sürecini güçlendirmek ve güvenlik açıklarını azaltmak amacıyla kullanılır.

Blacklisting'in ihtiyaç duyulmasının temel sebeplerinden biri, birisinin JWT'yi ele geçirmesi durumunda, kullanıcının çıkış yapmasına rağmen ele geçirilen tokenin hala kullanılarak API isteklerinin yapılabilmesidir. 
Bu durum, bir güvenlik açığına neden olabilir.

Bu sorunu çözmek için blacklisting yöntemi kullanılır. 
Blacklisting, geçerliliği sonlandırılan JWT'leri bir kara listeye alarak,
bunların artık geçerli olmadığını belirtir. 
Bir istemci tarafından gönderilen JWT, kara listede kontrol edilir.
Eğer JWT kara listede bulunuyorsa, istemciye 401 koduyla hata dönülür ve yetkilendirme işlemi reddedilir. 
Eğer JWT kara listede bulunmuyorsa, istemci işlemlerine devam eder.

## **Bu proje, Spring Boot ile bir JWT blacklisting uygulamasıdır.**

İlk olarak, tokenleri önbellekte (cache) tutmak için Caffeine Cache kullanılmıştır.
Gelen isteklerde önbellek kontrol edilir ve eğer token önbellekte bulunuyorsa, kullanıcıya 401 hata
koduyla geri dönülür. Eğer token önbellekte bulunmuyorsa, işlemler devam ettirilir.

`Bu projede ayrıca JWT (JSON Web Token) kullanılmıştır. Token'in geçerliliğini kontrol etmek için Interceptor'lardan da yararlanılmıştır.`