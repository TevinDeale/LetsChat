const express = require('express');
require('dotenv').config();
const { createLoggerInstance } = require('../../../shared/utils/logger')

const logger = createLoggerInstance('auth_service')

const env = process.env
