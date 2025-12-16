{{- define "ingress.name" -}}
{{- .Values.ingress.name | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "ingress.fullname" -}}
{{- printf "%s" .Chart.Name | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "ingress.services" -}}
- name: {{ printf "%s-service" (include "api.fullname" .) }}
  namespace: {{ .Values.namespace.api.name }}
  port: {{ .Values.api.ports.port }}
  path: {{ .Values.api.ingress.path }}
  pathType: {{ .Values.api.ingress.pathType }}
{{- end }}

