// ESTE SCRIPT FOI HERDADO DAS FASES PASSADAS DO PROJETO E PODE SER MODIFICADO CONFORME NECESSÁRIO
/* 
- Aguarde todos os PODs inicializarem antes de executar o teste de stress. Não somente o POD da API, mas também seus colaterais:
  - O EFK pode demorar um pouco para iniciar;
  - Acompanhe tudo via 'kubectl logs' - aguarde o log dos PODs indicarem que estão prontos;
  - Acompanhe tudo via 'kubectl top pods' - aguarde o consumo de CPU e memória estabilizar;
- Este teste de stress tenta simular um ataque de DDOs, enviando um usuário e senha inexistente para rota de login;
  - Pelo menos 1 request ao banco será feito, o que gerará de forma esperada um erro 404;
*/
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

export let check_failure_rate = new Rate('check_failure_rate');

export let options = {
  stages: [
    { duration: '6m', target: 100 }, // Aumenta gradualmente a carga para 100 usuários em 6 minutos
    { duration: '30s', target: 0 },  // Reduz a carga para 0 usuários em 30 segundos
  ],
  thresholds: {
    check_failure_rate: ['rate<0.01'], // Taxa de falhas dos checks deve ser menor que 1%
    http_req_duration: ['p(95)<1000'], // 95% das requisições devem ser concluídas em menos de 1000ms
  },
};

export default function () {
  const payload = JSON.stringify({
    email: "ddosattack@email.com",
    password: "passwdverystrong123"
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // Output Terraform: public_ip_fqdn
  const res = http.post('http://foodcoreapi2.eastus.cloudapp.azure.com/api/users/login', payload, params);

  const success = check(res, {
    'StatusCode é 404': (r) => r.status === 404, // Verifica se o status code é 404, como esperado
  });

  // Registra falha se o check falhar
  check_failure_rate.add(!success);

  sleep(1); // Aguarda 1 segundo entre as requisições para simular um ataque mais realista
}