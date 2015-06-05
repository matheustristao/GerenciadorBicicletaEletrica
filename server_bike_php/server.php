<?php



if (isset($_POST['numerofuncao'])) {
    $numero_funcao = $_POST['numerofuncao'];
} else
    $numero_funcao = $_GET['numerofuncao'];

//echo 'Numero funcao: ' .$numero_funcao. '  !!!';

$userDao     = new UsuarioDao();
$usuario     = new Usuario();
$userArduino = new Arduino();

switch ($numero_funcao) {
    case "1":
        $nome          = $_POST['nome'];
        $sobrenome     = $_POST['sobrenome'];
        $email         = $_POST['email'];
        $telefone      = $_POST['telefone'];
        $senha_usuario = $_POST['senha'];
        
        
        
        $userDao->saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario);
        break;
    case "2":
        $email         = $_POST['email'];
        $senha_usuario = $_POST['senha'];
        
        $userDao->checkLogin($email, $senha_usuario);
        break;
    case "3":
        $estacao       = $_POST['estacao'];
        $senha_usuario = $_POST['senha'];
        
        $usuario->habilitar_vaga($senha_usuario, $estacao);
        break;
    case "4":
        $estacao       = $_POST['estacao'];
        $senha_usuario = $_POST['senha'];
        
        $usuario->retirar_bike($senha_usuario, $estacao);
        break;
    case "5":
        $email = $_GET['email'];
        
        $userDao->listUsuario($email);
        break;
    
    case "6":
        $userArduino->checkSolicitacao();
        break;
    
    case "7":
        $tranca = $_POST['tranca'];
        $flag   = $_POST['flag'];
        
        $userArduino->updateSolicitacao($tranca, $flag);
        break;
}

//Classe responsável pela conexão com o banco
class ConnectionFactory
{
    
    static protected $mysqli;
    
    private function conectaBanco()
    {
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
    
    public function executaQuery($query)
    {
        $instanciaMySqli = self::conectaBanco();
        
        $instanciaMySqli->query($query);
    }
    
    public function listData($query)
    {
        $instanciaMySqli = self::conectaBanco();
        
        return $result = $instanciaMySqli->query($query);
    }
    
}

//Classe responsável pelo cadastro de usuários no banco
class UsuarioDao
{
    
    static private $connection;
    
    private static function instanciaConnection()
    {
        
        if (!isset(self::$connection)) {
            self::$connection = new ConnectionFactory();
        }
        
        return self::$connection;
    }
    
    
    
    public function checkLogin($email, $senha_usuario)
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.EMAIL like '$email'
                    and
                    P.SENHA like '$senha_usuario' 
                ";
        
        $result = $instanciaConnection->listData($query);
        
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
    
    
    public function checkLoginHash($senha_usuario)
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        $query = "select 
                1 
                from  USUARIO P
                where 
                    P.SENHA like '$senha_usuario' 
                ";
        
        $result = $instanciaConnection->listData($query);
        
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
    
    
    public function listUsuario($email)
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        $query = "select 
            P.NOME,
            P.SOBRENOME,
            P.EMAIL,
            P.TELEFONE 
                from  USUARIO P
                where 
                    P.EMAIL like '$email'
                ";
        
        $lista = $instanciaConnection->listData($query);
        
        $obj = $lista->fetch_object();
        echo "{ 'Nome' : '" . $obj->NOME . "', 'Sobrenome' : '" . $obj->SOBRENOME . "', 'Email' : '" . $obj->EMAIL . "', 'Telefone' : '" . $obj->TELEFONE . "' } ";
        
        return $obj;
    }
    
    public function saveUsuario($nome, $sobrenome, $email, $telefone, $senha_usuario)
    {
        
        echo "Dados recebidos e salvos no servidor";
        
        
        $instanciaConnection = self::instanciaConnection();
        
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
        
        $instanciaConnection->executaQuery($query1);
        
    }
    
}

class Usuario
{
    
    static private $connection;
    
    private static function instanciaConnection()
    {
        
        self::$connection = new ConnectionFactory();
        
        return self::$connection;
    }
    
    public function habilitar_vaga($senha, $estacao)
    {
        
        if ($userDao->checkLoginHash($senha) != 1) {
            echo "Liberação não autorizada: Senha não reconhecida";
            return -1;
        }
        
        $instanciaConnection = self::instanciaConnection();
        
        $userDao = new UsuarioDao();
        
        
        //Checagem de solicitação aberta
        $query_solicitacao = "select * from SOLICITACAO where DATA_FECHAMENTO is null or FLAG_ERRO is not null;";
        
        $result_solicitacao = $instanciaConnection->listData($query_solicitacao);
        
        $contagem = $result_solicitacao->num_rows;
        
        
        //Solicitação não pode ser feita e sai da função
        if ($contagem != 0) {
            echo "ERRO NO SERVIDOR, CONTATE ADMINISTADOR";
            return -2;
        }
        
        $solicitacao = "insert into SOLICITACAO 
                            (DATA_ABERTURA, 
                             ID_ARDUINO,
                             ID_TIPO)
                             VALUES
                             (SYSDATE(),
                              1,
                              1)";
        
        $instanciaConnection->executaQuery($solicitacao);
        
        //Tempo para Arduiino verificar se ha liberacao    
        sleep(10);
        
        
        $query_busca = "select ID_ESTACAO from ESTACAO where NOME like '$estacao';";
        
        $id_estacao = $instanciaConnection->listData($query_busca);
        
        if (self::checkVaga() == 1) {
            
            $query_insert = "insert into USUARIO_ESTACAO
                                (USUARIO_ID_USUARIO,
                                 ESTACAO_ID_ESTACAO,
                                 HORA_INICIO,
                                 HORA_FIM,
                                 CARGA_RESTANTE)
                            select ID_USUARIO,
                                   1,
                                   SYSDATE(),
                                   NULL,
                                   NULL
                            from   USUARIO 
                            where  USUARIO.SENHA like '$senha';";
            
            $instanciaConnection->executaQuery($query_insert);
            
            echo "Solicitação de acesso aprovada";
            
        } else {
            echo "Essa vaga está ocupada!";
        }
        
    }
    
    public function retirar_bike($senha, $estacao)
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        $userDao = new UsuarioDao();
        
        if ($userDao->checkLoginHash($senha) == 1) {
            
            $query_busca = "select ID_ESTACAO from ESTACAO where NOME like '$estacao';";
            
            $id_estacao = $instanciaConnection->listData($query_busca);
            
            if (self::checkVaga() == 0) {
                
                $query_insert = "update USUARIO_ESTACAO set 
                                HORA_FIM = SYSDATE()
                                where HORA_FIM is null
                                and USUARIO_ID_USUARIO = (select ID_USUARIO from USUARIO where SENHA like '$senha');";
                
                $instanciaConnection->executaQuery($query_insert);
                
                echo "Vaga liberada para novo uso, pode retirar a bike";
                
            } else {
                echo "Nada a fazer";
            }
        }
        
        else {
            echo "Liberação não autorizada: Senha não reconhecida";
        }
    }
    
    public static function checkVaga()
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        
        
        // Se retornar 0 possui vaga
        $query = "select * from USUARIO_ESTACAO where HORA_FIM is null;";
        
        $result = $instanciaConnection->listData($query);
        
        $contagem = $result->num_rows;
        
        if ($contagem == 0) {
            //Vaga pode ser utilizada de acordo com o BANCO na tabela USUAARIO_ESTACAO
            
            return 1;
        } else {
            // Não possui vagas
            return 0;
        }
        
        
    }
    
}

class Arduino
{
    
    static private $connection;
    
    private static function instanciaConnection()
    {
        
        self::$connection = new ConnectionFactory();
        
        return self::$connection;
    }
    
    public function checkSolicitacao()
    {
        
        $instanciaConnection = self::instanciaConnection();
        
        $query_solicitacao = "select * from SOLICITACAO where DATA_FECHAMENTO is null;";
        
        $result_solicitacao = $instanciaConnection->listData($query_solicitacao);
        
        $contagem = $result_solicitacao->num_rows;
        
        //Solicitação não pode ser feita e sai da função
        if ($contagem == 0) {
            echo "dbg: nao posso checar solicitacao: contagem=" . $contagem;
            return 0;
        }
        echo "dbg: posso checar: contagem=" . $contagem;
        return 1;
    }
    
    
    public function updateSolicitacao($tranca, $flag)
    {
        
        //tranca = 1 liberou energia para a tranca
        //tranca = 0 cortou energia para a tranca
        
        $id_tipo = 0;
        
        $instanciaConnection = self::instanciaConnection();
        
        if ($tranca == 1) {
            $id_tipo = 1;
        }
        if ($tranca == 2) {
            $id_tipo = 2;
        }
        
        if ($flag < 0) {
            
            $query_insert = "update `server_bike`.`SOLICITACAO` 
                                    set DATA_FECHAMENTO = SYSDATE(),
                                    FLAG_ERRO = $flag,
                                    id_tipo = $id_tipo
                                    where DATA_FECHAMENTO is null;";
            
            
            $instanciaConnection->executaQuery($query_insert);
            
            echo "dbg: caso com flag=" . $flag;
            
            return -1;
            
        }
        
        
        $query_insert = "update `server_bike`.`SOLICITACAO` 
                                set DATA_FECHAMENTO = SYSDATE(),
                                id_tipo = $id_tipo
                                where DATA_FECHAMENTO is null;";
        
        
        $instanciaConnection->executaQuery($query_insert);
        echo "dbg: atualizei SOLICITACAO tranca=" . $tranca . " flag=" . $flag;
        
        return 0;
    }
    
    
}


?>
