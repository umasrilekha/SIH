export const formatDate = (dateString?: string): string => {
  if (!dateString) return 'N/A';
  try {
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true,
    }).format(date);
  } catch (e) {
    return dateString;
  }
};

export const getRoleTitle = (role?: string): string => {
  switch (role) {
    case 'FOOD_SUPPLY_OFFICER':
      return 'Food Supply Officer';
    case 'SENIOR_OFFICER':
      return 'Senior Officer';
    case 'DEPARTMENT_ADMIN':
      return 'Department Administrator';
    case 'AUDITOR':
      return 'Auditor';
    default:
      return role || 'Officer';
  }
};

export const getStatusBadgeStyle = (status?: string) => {
  switch (status?.toUpperCase()) {
    case 'COMPLETED':
    case 'OPERATIONAL':
    case 'ACTIVE':
    case 'SUCCESS':
      return 'bg-emerald-100/70 text-emerald-900 border-emerald-300 font-semibold';
    case 'PENDING':
    case 'PENDING_VERIFICATION':
      return 'bg-amber-100/70 text-amber-900 border-amber-300 font-semibold';
    case 'PROCESSING':
      return 'bg-blue-100/70 text-blue-900 border-blue-300 font-semibold';
    case 'REJECTED':
    case 'FAILED':
    case 'LOCKED':
    case 'DOWN':
      return 'bg-rose-100/70 text-rose-900 border-rose-300 font-semibold';
    case 'NOT_CONFIGURED':
    case 'NOT CONNECTED':
      return 'bg-slate-100 text-slate-700 border-slate-300 font-medium';
    default:
      return 'bg-slate-100 text-slate-700 border-slate-300 font-medium';
  }
};
