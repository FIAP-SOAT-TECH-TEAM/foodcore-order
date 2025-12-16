{{- define "storageclass.name" -}}
{{- .Values.storageClass.name | trunc 63 | trimSuffix "-" }}
{{- end }}

{{- define "storageclass.fullname" -}}
{{- printf "%s" .Chart.Name | trunc 63 | trimSuffix "-" }}
{{- end }}