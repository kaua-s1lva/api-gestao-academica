package br.ufes.ccens.core.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.jboss.logging.Logger;

@LogTransaction
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
public class LogTransactionInterceptor {
    private static final Logger LOG = Logger.getLogger(LogTransactionInterceptor.class);

    @AroundInvoke
    public Object logMethod(InvocationContext context) throws Exception {
        String className = context.getTarget().getClass().getSimpleName();
        String methodName = context.getMethod().getName();
        
        long start = System.currentTimeMillis();
        LOG.infof("➡️ Iniciando execução: %s.%s()", className, methodName);

        try {
            Object result = context.proceed(); 
            
            long tempoExecucao = System.currentTimeMillis() - start;
            LOG.infof("✅ Sucesso: %s.%s() finalizado em %d ms", className, methodName, tempoExecucao);
            
            return result;
        } catch (Exception e) {
            long tempoExecucao = System.currentTimeMillis() - start;
            LOG.errorf("❌ Erro em %s.%s() após %d ms. Motivo: %s", className, methodName, tempoExecucao, e.getMessage());
            throw e;
        }
    }
}
