## Sobre o projeto:
● App do desafio da empresa ioasys (https://bitbucket.org/ioasys/empresas-android/src/master/).
### Enterprise_app é uma aplicação mobile composto por 3 telas:

1. Tela de login: Foram tratados os casos de erro em que o email e a senha foram inválidos. Foi utilizado o padrão OAuth 2.0 e no caso de sucesso do login a api retornou 3 custom headers (access-token, uid, client). Usuário: testeapple@ioasys.com.br / Senha: 12341234;

2. Tela de listagem das empresas: Nessa tela foi feita uma requisição para obter a lista de empresas e utilizado uma endpoint para filtrar a mesma. Foram utilizados os headers obtidos da tela anterior para autenticar essa requisição;

3. Tela de descrição de uma empresa: Nessa tela são exibidos em detalhe as informações de uma empresa clicada na lista da tela anterior.

## Principais bibliotecas utilizadas:
● Retrofit <br/> 
● Glide <br/> 

## Módulos:
● App <br/> 
● Core <br/> 
● Navigation <br/> 
● Feature Auth <br/> 
● Feature Home <br/> 
● Data source <br/> 

## Tecnologias Utilizadas
● Kotlin <br/> 
● Arquitetura MVVM <br/> 

## Implantação em desenvolvimento
● Injeção de dependência com Koin <br/> 
● Coroutines <br/> 


## Layout Mobile
 <div align="center">
  <img src= "https://user-images.githubusercontent.com/102394401/205459180-9b95f878-1da8-4898-a4c5-ea9be8fa3877.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459181-83f83a4c-2381-4971-83ba-1200d30577d0.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459182-43f87fc9-235f-4e53-a2c1-aa12a0277f28.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459183-8f7afc84-a70e-4b62-9f84-2b52c281df78.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459184-0001cd7c-91a6-46b2-926e-aeefa8662305.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459185-63808303-7660-4845-95d9-3a0114075ddd.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459188-744cfcd8-8e2c-4d00-a9e9-f2c25d35b08c.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459190-86029627-2577-46c8-becc-26a7c0216bd7.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459191-76019a2d-442f-4203-ae3b-8aebaa4e2fa1.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459194-93224998-b07c-446b-8570-6e2f2efeb9fa.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459196-2277c6ac-11a4-4c1b-94ef-01c35d9b65d9.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459198-e13c1e6b-6ac6-4256-96c0-d0717cf20984.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459199-994060b1-5b24-479c-b916-cff0cd962be5.png" width="300"/> 
  <img src= "https://user-images.githubusercontent.com/102394401/205459202-43ecc777-e9c8-4b71-8053-3710c42da4b8.png" width="300"/> 
  </div>
  
  
