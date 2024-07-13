const { ExpressAuth } = require('@auth/express');
const express = require('express');
require('dotenv').config();
const { createLoggerInstance } = require('../../../shared/utils/logger')

// set up logger util
const logger = createLoggerInstance('auth_service')

const app = express()

app.set('trust proxy', true)
app.use("/auth/*", ExpressAuth({ providers: [] }))
