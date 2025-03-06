package com.jack.accounts.audit;

import com.jack.accounts.constant.AccountsConstants;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {
  @Override
  @NonNull
  public Optional<String> getCurrentAuditor() {
    return Optional.of(AccountsConstants.ACCOUNT_MS);
  }
}
