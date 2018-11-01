# ofx-tax-schema-and-models-2018

Unofficial copy of Open Financial Exchange (OFX) tax document schema (.xsd) files

The official files are downloadable from: http://www.ofx.org/downloads.html.

An Apache Maven pom.xml file is included to generate model files used with Java JAXB implementations of the schema.

Note: For tax year 2018, the tax schemas are based on OFX 2.2 which includes OAuth token support.

For more information, send an email to support@itips.info.


## Folders

All 2018 tax schema files:

```
src/main/resources/ofx-tax-2018-all
```

"Core" 2018 tax schema files:

```
src/main/resources/ofx-tax-2018-core
```

"Add on" 2018 Schedules K-1 tax schema files:

```
src/main/resources/ofx-tax-2018-k1
```

"Add on" 2018 Additional Forms tax schema files:

```
src/main/resources/ofx-tax-2018-addl
```

