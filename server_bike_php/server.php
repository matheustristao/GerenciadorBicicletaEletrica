<?php


    $numero_funcao = $_POST['numerofuncao'];
    

    //echo 'Numero funcao: ' .$numero_funcao. '  !!!';

    $userDao = new UsuarioDao();
    $usuario = new Usuario();

    switch ($numero_funcao) {
    case "1":
        $nome = $_POST['nome'];
        $sobrenome = $_POST['sobrenome'];
        $email = $_POST['email'];
        $telefone = $_POST['telefone'];
        $senha_usuario = $_POST['senha'];

        $userDao->saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario);
        break;
    case "2":
        $email = $_POST['email'];
        $senha_usuario = $_POST['senha'];    

        $userDao->checkLogin($email, $senha_usuario);
        break;
    case "3":
        $senha_usuario = $_POST['senha'];    

        $usuario->liberar_catraca($senha_usuario);
        break;    
}

    //Classe responsável pela conexão com o banco
    class ConectionFactory {

    static protected $mysqli;
    
    private function conectaBanco() {
        $MySQL = array(
            'servidor' => 'localhost',
            'usuario' => 'root',
            'senha_banco' => 'root',
            'banco' => 'server_bike'
        );

        $mysqli = new mysqli($MySQL['servidor'], $MySQL['usuario'], $MySQL['senha_banco'], $MySQL['banco']);

        if (mysqli_connect_errno()) {
            trigger_error(mysqli_connect_error(), E_USER_ERROR);
        } else {
            return $mysqli;
        }
    }

    public function executaQuery($query) {
        $instanciaMySqli = self::conectaBanco();

        $instanciaMySqli->query($query);
    }

    public function listData($query) {
        $instanciaMySqli = self::conectaBanco();

        return $result = $instanciaMySqli->query($query);
    }

    }

    //Classe responsável pelo cadastro de usuários no banco
    class UsuarioDao {

    static private $conection;

    private static function instanciaConection() {

        if (!isset(self::$conection)) {
            self::$conection = new ConectionFactory();
        }

        return self::$conection;
    }



    public function checkLogin($email, $senha_usuario) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.EMAIL like '$email'
                    and
                    P.SENHA like '$senha_usuario' 
                ";

         $result = $instanciaConection->listData($query);
         
         $contagem = $result->num_rows;  

         //echo $result;      

        if ($contagem > 0) {
            echo "loguei";
            return 1;
        } else {
            echo "naologuei";
            return 0;
        }
    }

    public function checkLoginHash( $senha_usuario) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.SENHA like '$senha_usuario' 
                ";

         $result = $instanciaConection->listData($query);
         
         $contagem = $result->num_rows;  

         //echo $result;      
        if ($contagem > 0) {
            //achou
            return 1;
        } else {
            // nao achou
            return 0;
        }
    }

    public function listUsuario($nome,$sobrenome, $telefone, $email) {

        $instanciaConection = self::instanciaConection();

        $query = "select 
            P.NOME,
            P.SOBRENOME,
            P.EMAIL,
            P.TELEFONE 
                from  usuario P
                where 
                    P.NOME like '$nome'
                    and
                    P.SOBRENOME like '$sobrenome' 
                ;";

        return $lista = $instanciaConection->listData($query);
    }

    public function saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario) {

        echo "Dados recebidos e salvos no servidor";


        $instanciaConection = self::instanciaConection();

        $query1 = "insert into USUARIO (
                     NOME
                    ,SOBRENOME
                    ,EMAIL
                    ,TELEFONE
                    ,SENHA
                    ) VALUES ( 
                     '$nome' 
                    ,'$sobrenome' 
                    ,'$email'
                    ,'$telefone'
                    ,'$senha_usuario' 
                    );";

        $instanciaConection->executaQuery($query1);
            
    }

    }

    class Usuario
    {
        public function liberar_catraca($senha){

             $instanciaConection = self::instanciaConection();

            $userDao = new UsuarioDao();  

            if ($userDao->checkLoginHash($senha) == 1){

                  if(self->checkVaga() == 1){
                    
                        $query = "insert into USUARIO_ESTACAO
                                (USUARIO_ID_USUARIO,
                                 ESTACAO_ID_ESTACAO,
                                 HORA_INICIO,
                                 HORA_FIM,
                                 CARGA_RESTANTE)
                            SELECT ID_USUARIO,
                                    1,
                                   SYSDATE(),
                                   NULL,
                                   NULL
                            FROM   USUARIO 
                            WHERE  SENHA LIKE '$senha';";

                $instanciaConection->executaQuery($query);

                echo "Vaga Liberada!";
                
                  }
                  else{
                     echo "Essa vaga está ocupada!";
                  }
            }

            else{
                echo "Liberação não autorizada: Senha não reconhecida";
            }
        }

        public static function checkVaga(){

           $instanciaConection = self::instanciaConection();

                $query = "select 1 from USUARIO_ESTACAO where HORA_FIM is null;";

             $result = $instanciaConection->listData($query);

             $contagem = $result->num_rows;        

            if ($contagem > 0) {
                // Nao possui vagas
                return 0;
            } else {
                // possui vagas
                return 1;
            }

        }

    }

?>
